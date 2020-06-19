using System;
using MeepleClient.Network;
using UnityEngine;

namespace MeepleClient.Commands
{
    [Serializable]
    public class FlipCommand : IInvocable, IMessageConvertible
    {
        [SerializeField] private Item item;

        public Item Item
        {
            get => item;
            set => item = value;
        }
        
        public FlipCommand(Item item)
        {
            this.Item = item;
        }
        
        public void Invoke()
        {
            Item.AnimateFlip();
        }

        public Message ToMessage()
        {
            return new FlipMessage(item.Guid.ToString());
        }
    }
}