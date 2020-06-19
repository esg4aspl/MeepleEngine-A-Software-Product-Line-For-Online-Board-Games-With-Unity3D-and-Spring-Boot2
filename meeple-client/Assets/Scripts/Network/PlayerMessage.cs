namespace MeepleClient.Network
{
    public class PlayerMessage : Message
    {
        public override string Channel { get; } = "Player";
        
        // ReSharper disable once MemberCanBePrivate.Global
        // ReSharper disable once UnusedAutoPropertyAccessor.Global
        public PlayerData Data { get; set;  } // Serializer uses it

        public PlayerMessage(string playerId)
        {
            Data = new PlayerData()
            {
                PlayerId = playerId
            };
        }
    }

    public class PlayerData
    {
        public string PlayerId { get; set; }
    }
}