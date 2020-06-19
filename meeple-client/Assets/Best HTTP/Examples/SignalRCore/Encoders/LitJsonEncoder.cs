#if !BESTHTTP_DISABLE_SIGNALR_CORE
using System;
using BestHTTP.PlatformSupport.Memory;
using LitJson;

namespace BestHTTP.SignalRCore.Encoders
{
    public sealed class LitJsonEncoder : BestHTTP.SignalRCore.IEncoder
    {
        public LitJsonEncoder()
        {
            LitJson.JsonMapper.RegisterImporter<int, long>((input) => input);
            LitJson.JsonMapper.RegisterImporter<long, int>((input) => (int)input);
            LitJson.JsonMapper.RegisterImporter<double, int>((input) => (int)(input + 0.5));
            LitJson.JsonMapper.RegisterImporter<string, DateTime>((input) => Convert.ToDateTime((string)input).ToUniversalTime());
            LitJson.JsonMapper.RegisterImporter<double, float>((input) => (float)input);
            LitJson.JsonMapper.RegisterImporter<string, byte[]>((input) => Convert.FromBase64String(input));
        }

        public T DecodeAs<T>(BufferSegment buffer)
        {
            using (var reader = new System.IO.StreamReader(new System.IO.MemoryStream(buffer.Data, buffer.Offset, buffer.Count)))
            {
                return JsonMapper.ToObject<T>(reader);
            }
        }

        public PlatformSupport.Memory.BufferSegment Encode<T>(T value)
        {
            var json = JsonMapper.ToJson(value);
            int len = System.Text.Encoding.UTF8.GetByteCount(json);
            byte[] buffer = BufferPool.Get(len + 1, true);
            System.Text.Encoding.UTF8.GetBytes(json, 0, json.Length, buffer, 0);
            buffer[len] = 0x1e;
            return new BufferSegment(buffer, 0, len + 1);
        }

        public object ConvertTo(Type toType, object obj)
        {
            string json = LitJson.JsonMapper.ToJson(obj);
            return LitJson.JsonMapper.ToObject(toType, json);
        }
    }
}

#endif
