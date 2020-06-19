using MeepleClient.Commands;

namespace MeepleClient.Network
{
    public class FlipMessage : Message, ICommandConvertible
    {
        public override string Channel { get; } = "Flip";
        public FlipData Data { get; set; }

        public FlipMessage(string objectId)
        {
            Data = new FlipData()
            {
                ObjectId = objectId
            };
        }

        public IInvocable ToCommand()
        {
            var meepleObject = GameWorld.FindMeepleObjectByGuid(Data.ObjectId);
            return new FlipCommand((Item) meepleObject);
        }
    }

    public class FlipData
    {
        public string ObjectId { get; set; }
    }
}