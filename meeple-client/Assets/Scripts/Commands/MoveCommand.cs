using System;
using MeepleClient.Commands;
using MeepleClient.Network;
using UnityEngine;

namespace MeepleClient
{
    [Serializable]
    public class MoveCommand : IInvocable, IMessageConvertible
    {
        [SerializeField] private Item _item;
        [SerializeField] private Grid _origin;
        [SerializeField] private Grid _destination;
        private bool _animate;
        
        public MoveCommand(Item item, IPlaceable placeable) : this(item, placeable.GetDestination())
        {
        }

        public MoveCommand(Item item, Grid destination, bool animate=true)
        {
            _item = item;
            _origin = _item.CurrentGrid;
            _destination = destination;
            _animate = animate;
        }

        public void Invoke()
        {
            Debug.Log("MoveObjectCommand Invoked");

            // remove meeple object from its current grid
            if (_item.CurrentGrid != null)
            {
                _item.CurrentGrid.Remove(_item);
            }

            // Put meeple object to new grid
            var slot = _destination.GetAvailableSlot(_item);

            slot.Item = _item;

            // Set current grid to destination grid
            _item.CurrentGrid = _destination;

            _item.transform.SetParent(slot.transform);

            // // Start animation
            if (_animate)
            {
                _item.AnimateMove(slot.Position, slot.Rotation);
            }
            else
            {
                _item.transform.position = slot.Position;
                // _item.transform.rotation = Quaternion.Euler(new Vector3(slot.Rotation.x, slot.Rotation.y, _item.transform.rotation.z));
            }
        }

        public void Revoke()
        {
            new MoveCommand(_item, _origin).Invoke();
        }

        public Item Item
        {
            get => _item;
            set => _item = value;
        }

        public Grid Origin
        {
            get => _origin;
            set => _origin = value;
        }

        public Grid Destination
        {
            get => _destination;
            set => _destination = value;
        }

        public Message ToMessage()
        {
            return new MoveMessage(_item.Guid.ToString(), _destination.Guid.ToString());
        }
    }
}