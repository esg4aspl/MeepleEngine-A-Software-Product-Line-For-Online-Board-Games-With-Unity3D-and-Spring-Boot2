using System;
using System.Linq;
using MeepleClient.Serializables;
using UnityEngine;

namespace MeepleClient
{
    public class Deck : Item, IPlaceable
    {
        [SerializeField] private DeckSerializable deckData;
        
        private Grid _grid;
        private BoxCollider _gridCollider;
        private BoxCollider _collider;
        private float _width;
        private float _height;
        // [SerializeField] private Grid gridPrefab;

        // private bool _canShow;
        // private Vector3 _menuPosition;
        // [SerializeField] private Hand hand;

        public void Initialize(DeckSerializable serializable)
        {
            base.Initialize(serializable);
            _collider = GetComponent<BoxCollider>();
        }
        
        public void Initialize(Vector3 size, Grid currentGrid)
        {
            transform.position = Vector3.up * size.y / 2;
            transform.localScale = size;
            CurrentGrid = currentGrid;
        }

        // public override MeepleObjectSerializable GetSerializable()
        // {
        //     
        // }

        public override MeepleObjectSerializable GetSerializable()
        {
            deckData.Guid = GetInstanceID();
            deckData.Name = gameObject.name;
            deckData.Size = transform.localScale;
            deckData.Rotation = transform.rotation.eulerAngles;
            deckData.CurrentGridGuid = CurrentGrid.GetInstanceID();
            return deckData;
        }

        public override void AddGrid(Grid newGrid)
        {
            _grid = newGrid;
            _grid.OnInsert += Insert;
            _grid.OnRemove += RemoveCard;
            _gridCollider = _grid.GetComponent<BoxCollider>();
        }

        /// <summary>
        /// Insert card to top of deck
        /// </summary>
        /// <param name="item"></param>
        private Slot Insert(Item item)
        {
            var card = item as Card;
            if (card == null)
            {
                throw new Exception("Only cards can be added to deck");
            }

            return Insert(card, _grid.Slots.Count);
        }

        /// <summary>
        /// Insert card to given index
        /// </summary>
        /// <param name="card"></param>
        /// <param name="index"></param>
        private Slot Insert(Card card, int index)
        {
            _width = card.transform.localScale.x;
            _height = card.transform.localScale.z;
            if (index > _grid.Slots.Count || index < 0)
            {
                index = _grid.Slots.Count;
            }

            

            Vector3 position;
            if (_grid.Slots.Count == 0)
            {
                position = _grid.transform.position;
            }
            else if (index == _grid.Slots.Count)
            {
                var targetSlot = _grid.Slots[index - 1];
                position = targetSlot.transform.position + Vector3.up * card.Thickness;
            }
            else
            {
                var targetSlot = _grid.Slots[index];
                position = targetSlot.transform.position;
                for (var i = index; i < _grid.Slots.Count; i++)
                {
                    _grid.Slots[i].transform.position += Vector3.up * card.Thickness;
                }
            }

            var createdSlot = _grid.CreateSlot(position);
            _grid.Slots.Insert(index, createdSlot);
            UpdateCollider();
            return createdSlot;
        }


        private void RemoveCard(Item item, Slot slot)
        {
            Debug.Log("Removing card from deck");
            var card = item as Card;
            if (card == null)
            {
                throw new Exception("Only cards can be removed from deck");
            }
            // Dont remove last empty slot
            if(_grid.Slots.Count == 1) return;;
            
            var slotIndex = _grid.Slots.IndexOf(slot);
            // Update position of slots and items that above of slot
            // for (var i = slotIndex; i < grid.Slots.Count; i++)
            // {
            //     grid.Slots[i].transform.position += Vector3.down * card.Thickness;
            //     grid.Slots[i].Item.transform.position = grid.Slots[i].transform.position; 
            // }
            // Remove empty slot from slot list and destroy it
            _grid.Slots.Remove(slot);
            Destroy(slot);
            UpdateCollider();
        }

        private void UpdateCollider()
        {
            var length = _grid.Slots[_grid.Slots.Count-1].transform.position.y - _grid.Slots[0].transform.position.y;
            // var card = _grid.Slots[0].Item
            _gridCollider.center = Vector3.up * length / 2;
            _gridCollider.size = new Vector3(_width, length + 0.06f, _height);
            _collider.center = Vector3.up * length / 2;
            _collider.size = new Vector3(_width, length + 0.06f, _height) + Vector3.one * 0.001f;
        }
        // public void ShowActionMenu()
        // {
        //     _canShow = true;
        //     _menuPosition = Input.mousePosition;
        // }
        //
        // public void CloseActionMenu()
        // {
        //     _canShow = false;
        // }
        //
        // private void OnGUI()
        // {
        //     if (_canShow && GUI.Button(new Rect(_menuPosition.x, Screen.height - _menuPosition.y, 50, 50), "Draw"))
        //     {
        //         // var card = RemoveCard();
        //         // hand.AddCard(card);
        //         _canShow = false;
        //     }
        // }

        public Grid GetDestination()
        {
            return _grid;
        }
    }
}