using System;
using System.Collections.Generic;
using BestHTTP.WebSocket;
using MeepleClient.Commands;
using MeepleClient.Network;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

using UnityEngine;

namespace MeepleClient
{
    public class MeepleWebSocket : MonoBehaviour
    {
        public GameWorld.IdObject objects;
        [SerializeField] private InfoController infoController;
        [SerializeField] private MoveCommand moveCommand;
        [SerializeField] private FlipCommand flipCommand;
        [SerializeField] private string playerId;

        [SerializeField] public string url = "ws://127.0.0.1:4567/socket.io/?EIO=4&transport=websocket";
        [SerializeField] private WebSocket webSocket;
        [SerializeField, ReadOnly] private int ping;

        [SerializeField] private Queue<string> messageQueue = new Queue<string>();

        private JsonSerializerSettings _serializerSettings;

        private void Awake()
        {
            _serializerSettings = new JsonSerializerSettings
            {
                ContractResolver = new CamelCasePropertyNamesContractResolver()
            };
        }

        private void Start()
        {
            webSocket = new WebSocket(new Uri(url));
            webSocket.OnOpen += OnOpen;
            webSocket.OnMessage += OnMessage;
            webSocket.OnError += OnError;
            webSocket.OnClosed += OnClosed;
            webSocket.StartPingThread = true;
            webSocket.Open();
        }

        public void SendAction(IMessageConvertible command)
        {
            var messageString = JsonConvert.SerializeObject(command.ToMessage(), _serializerSettings);
            Debug.Log($"Sending action ... {messageString}");
            webSocket.Send(messageString);
        }

        public void SendMessage(Message message)
        {
            var messageString = JsonConvert.SerializeObject(message, _serializerSettings);
            Debug.Log($"Sending message ... {messageString}");
            webSocket.Send(messageString);
        }

        public void SendMove()
        {
            var moveMessage = new MoveMessage(moveCommand.Item.Guid.ToString(), moveCommand.Destination.Guid.ToString());
            var messageString = JsonConvert.SerializeObject(moveMessage, _serializerSettings);
            Debug.Log(messageString);
            Debug.Log("Sending");
            webSocket.Send(messageString);
        }

        public void SendFlip()
        {
            var moveMessage = new FlipMessage(flipCommand.Item.Guid.ToString());
            var messageString = JsonConvert.SerializeObject(moveMessage, _serializerSettings);
            Debug.Log(messageString);
            Debug.Log("Sending");
            webSocket.Send(messageString);
        }

        private void SendPlayerId()
        {
            var playerMessage = new PlayerMessage(playerId);
            var message = JsonConvert.SerializeObject(playerMessage, _serializerSettings);
            Debug.Log("Sending " + message);
            webSocket.Send(message);
           
        }

        public void SendReady()
        {
            Debug.Log("Sending click button name ready");
            webSocket.Send("{\"channel\":\"Info\", \"data\":{\"message\":\"Ready\"}}");
        }

        private void FixedUpdate()
        {
            objects = GameWorld.MeepleObjects;
            if (webSocket != null && webSocket.IsOpen)
            {
                ping = webSocket.Latency;
                if (messageQueue.Count == 0) return;
                var messageString = messageQueue.Dequeue();
                var message = JsonConvert.DeserializeObject<Message>(messageString, _serializerSettings);
                if (message is ICommandConvertible commandMessage)
                {
                    commandMessage.ToCommand().Invoke();
                }
                else if (message is InfoMessage infoMessage)
                {
                    infoController.GiveInfo(infoMessage.Data.Message);
                }
                else if (message != null)
                {
                    Debug.Log($"Non command message type {messageString}");
                }
                else 
                {
                    Debug.Log($"Unsupported message type {messageString}");
                }
            }
        }

        // Update is called once per frame
        private void OnOpen(WebSocket ws)
        {
            Debug.Log("-WebSocket Open!\n");
            SendPlayerId();
        }

        private void OnMessage(WebSocket ws, string message)
        {
            Debug.Log($"Message received: {message}");
            messageQueue.Enqueue(message);
        }
        
        private void OnClosed(WebSocket ws, UInt16 code, string message)
        {
            Debug.Log($"-WebSocket closed! Code: {code} Message: {message}");
            webSocket = null;
        }

        private void OnError(WebSocket ws, string error)
        {
            Debug.LogError($"-An error occured: {error}");
            webSocket = null;
        }

        private void OnDestroy()
        {
            if (webSocket != null)
            {
                webSocket.Close();
                webSocket = null;
            }
        }
    }
}