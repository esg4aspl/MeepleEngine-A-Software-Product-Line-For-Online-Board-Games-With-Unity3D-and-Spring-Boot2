using JsonSubTypes;
using MeepleClient.Commands;
using Newtonsoft.Json;

namespace MeepleClient.Network
{
    [JsonConverter(typeof(JsonSubtypes), "Channel")]
    [JsonSubtypes.KnownSubType(typeof(MoveMessage), "Move")]
    [JsonSubtypes.KnownSubType(typeof(PlayerMessage), "Player")]
    [JsonSubtypes.KnownSubType(typeof(FlipMessage), "Flip")]
    [JsonSubtypes.KnownSubType(typeof(SelectMessage), "Select")]
    [JsonSubtypes.KnownSubType(typeof(InfoMessage), "Info")]
    [JsonSubtypes.KnownSubType(typeof(ClickMessage), "Click")]
    public abstract class Message
    {
        public virtual string Channel { get; }
    }
}