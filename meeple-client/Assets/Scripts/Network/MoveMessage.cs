using MeepleClient.Commands;
using UnityEngine;

namespace MeepleClient.Network
{
    public class MoveMessage : Message, ICommandConvertible
    {
        public override string Channel { get; } = "Move";
        
        // ReSharper disable once MemberCanBePrivate.Global
        public MoveData Data { get; set; } // Serializer uses it

        public MoveMessage(string objectId, string gridId)
        {
            Data = new MoveData
            {
                ObjectId = objectId,
                GridId = gridId
            };
        }

        public IInvocable ToCommand()
        {
            // var moveMessage = (MoveMessage)JsonConvert.DeserializeObject<Message>(this);
            
            var objectId = Data.ObjectId;
            var gridId = Data.GridId;

            var meepleObject = GameWorld.FindMeepleObjectByGuid(objectId);
            var destination = GameWorld.FindMeepleObjectByGuid(gridId);
            return new MoveCommand((Item) meepleObject, (IPlaceable) destination);
        }
    }

    public class MoveData
    {
        public string ObjectId { get; set; }
        public string GridId { get; set; }
    }
}