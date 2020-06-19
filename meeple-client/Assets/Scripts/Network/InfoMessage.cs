namespace MeepleClient.Network
{
    public class InfoMessage : Message
    {
        public override string Channel { get; } = "Info";
        public InfoData Data { get; set; }

        public InfoMessage(string message)
        {
            Data = new InfoData()
            {
                Message = message
            };
        }
    }

    public class InfoData
    {
        public string Message { get; set; }
    }
}