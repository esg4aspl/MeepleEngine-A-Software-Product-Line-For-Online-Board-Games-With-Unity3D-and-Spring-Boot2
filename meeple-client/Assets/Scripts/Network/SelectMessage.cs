using MeepleClient.Commands;

namespace MeepleClient.Network
{
    public class SelectMessage: Message
    {
        public override string Channel { get; } = "Select";
        public SelectData Data { get; set; }

        public SelectMessage(string objectId)
        {
            Data = new SelectData()
            {
                ObjectId = objectId
            };
        }
    }

    public class SelectData
    {
        public string ObjectId { get; set; }
    }
}