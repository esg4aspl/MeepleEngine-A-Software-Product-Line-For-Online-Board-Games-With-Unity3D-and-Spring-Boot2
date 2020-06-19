#if !BESTHTTP_DISABLE_SIGNALR_CORE && !BESTHTTP_DISABLE_WEBSOCKET
using System;
using System.Collections.Generic;
using System.Text;

using BestHTTP.PlatformSupport.Memory;
using BestHTTP.SignalRCore.Messages;

namespace BestHTTP.SignalRCore.Transports
{
    /// <summary>
    /// WebSockets transport implementation.
    /// https://github.com/aspnet/SignalR/blob/dev/specs/TransportProtocols.md#websockets-full-duplex
    /// </summary>
    internal sealed class WebSocketTransport : TransportBase
    {
        public override TransportTypes TransportType { get { return TransportTypes.WebSocket; } }

        private WebSocket.WebSocket webSocket;

        internal WebSocketTransport(HubConnection con)
            :base(con)
        {
        }

        public override void StartConnect()
        {
            HTTPManager.Logger.Verbose("WebSocketTransport", "StartConnect");

            if (this.webSocket == null)
            {
                Uri uri = BuildUri(this.connection.Uri);

                // Also, if there's an authentication provider it can alter further our uri.
                if (this.connection.AuthenticationProvider != null)
                    uri = this.connection.AuthenticationProvider.PrepareUri(uri) ?? uri;

                HTTPManager.Logger.Verbose("WebSocketTransport", "StartConnect connecting to Uri: " + uri.ToString());

                this.webSocket = new WebSocket.WebSocket(uri);
            }

#if !UNITY_WEBGL || UNITY_EDITOR
            // prepare the internal http request
            if (this.connection.AuthenticationProvider != null)
                this.connection.AuthenticationProvider.PrepareRequest(webSocket.InternalRequest);
#endif
            this.webSocket.OnOpen += OnOpen;
            this.webSocket.OnMessage += OnMessage;
            this.webSocket.OnBinary += OnBinary;
            this.webSocket.OnError += OnError;
            this.webSocket.OnClosed += OnClosed;

            this.webSocket.Open();
            
            this.State = TransportStates.Connecting;
        }

        public override void Send(BufferSegment msg)
        {
            if (this.webSocket == null)
                return;

            this.webSocket.Send(msg.Data, (ulong)msg.Offset, (ulong)msg.Count);

            BufferPool.Release(msg.Data);
        }

        // The websocket connection is open
        private void OnOpen(WebSocket.WebSocket webSocket)
        {
            HTTPManager.Logger.Verbose("WebSocketTransport", "OnOpen");

            // https://github.com/aspnet/SignalR/blob/dev/specs/HubProtocol.md#overview
            // When our websocket connection is open, send the 'negotiation' message to the server.
            (this as ITransport).Send(JsonProtocol.WithSeparator(string.Format("{{\"protocol\":\"{0}\", \"version\": 1}}", this.connection.Protocol.Name)));
        }

        private void OnMessage(WebSocket.WebSocket webSocket, string data)
        {
            if (this.State == TransportStates.Connecting)
            {
                HandleHandshakeResponse(data);

                return;
            }

            this.messages.Clear();
            try
            {
                int len = System.Text.Encoding.UTF8.GetByteCount(data);

                byte[] buffer = BufferPool.Get(len, true);
                try
                {
                    Array.Clear(buffer, 0, buffer.Length);

                    System.Text.Encoding.UTF8.GetBytes(data, 0, data.Length, buffer, 0);

                    this.connection.Protocol.ParseMessages(new BufferSegment(buffer, 0, len), ref this.messages);
                }
                finally
                {
                    BufferPool.Release(buffer);
                }                

                this.connection.OnMessages(this.messages);
            }
            catch (Exception ex)
            {
                HTTPManager.Logger.Exception("WebSocketTransport", "OnMessage(string)", ex);
            }
            finally
            {
                this.messages.Clear();
            }
        }

        private void OnBinary(WebSocket.WebSocket webSocket, byte[] data)
        {
            if (this.State == TransportStates.Connecting)
            {
                HandleHandshakeResponse(System.Text.Encoding.UTF8.GetString(data, 0, data.Length));

                return;
            }

            this.messages.Clear();
            try
            {
                this.connection.Protocol.ParseMessages(new BufferSegment(data, 0, data.Length), ref this.messages);

                this.connection.OnMessages(this.messages);
            }
            catch (Exception ex)
            {
                HTTPManager.Logger.Exception("WebSocketTransport", "OnMessage(byte[])", ex);
            }
            finally
            {
                this.messages.Clear();

                BufferPool.Release(data);
            }
        }

        private void OnError(WebSocket.WebSocket webSocket, string reason)
        {
            HTTPManager.Logger.Verbose("WebSocketTransport", "OnError: " + reason);

            if (this.State == TransportStates.Closing)
            {
                this.State = TransportStates.Closed;
            }
            else
            {
                this.ErrorReason = reason;
                this.State = TransportStates.Failed;
            }
        }

        private void OnClosed(WebSocket.WebSocket webSocket, ushort code, string message)
        {
            HTTPManager.Logger.Verbose("WebSocketTransport", "OnClosed: " + code + " " + message);

            this.webSocket = null;

            this.State = TransportStates.Closed;
        }

        public override void StartClose()
        {
            HTTPManager.Logger.Verbose("WebSocketTransport", "StartClose");

            if (this.webSocket != null)
            {
                this.State = TransportStates.Closing;
                this.webSocket.Close();
            }
            else
                this.State = TransportStates.Closed;
        }
    }
}
#endif
