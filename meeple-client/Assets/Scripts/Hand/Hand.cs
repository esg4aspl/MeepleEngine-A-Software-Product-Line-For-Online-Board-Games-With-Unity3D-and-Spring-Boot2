using System;
using MeepleClient.Serializables;
using UnityEngine;

namespace MeepleClient
{
    public class Hand : Item, IPlaceable
    {
        [SerializeField] private HandSerializable handData;
        [SerializeField, ReadOnly] private Grid grid;
        // private int maxWidth = 800;
        private float _spacing = 3.2f;

        public void Initialize(HandSerializable serializable)
        {
            base.Initialize(serializable);
            handData = serializable;
            _spacing = serializable.Spacing;
            if (handData.Active)
            {
                var a = FindObjectOfType(typeof(ActionManager)) as ActionManager;
                a.HandController = this.GetComponentInChildren<HandController>();
                Debug.LogWarning("A");
            }
            else
            {
                transform.GetChild(0).gameObject.SetActive(false);
            }
        }
        
        public void Initialize(Vector3 size, Grid currentGrid, int spacing)
        {
            // transform.
            transform.localScale = size;
            CurrentGrid = currentGrid;
            _spacing = spacing;
        }

        public override void AddGrid(Grid newGrid)
        {
            grid = newGrid;
            grid.OnInsert += Insert;
            grid.OnRemove += RemoveCard;
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            handData.Guid = GetInstanceID();
            handData.Name = gameObject.name;
            handData.Size = transform.localScale;
            handData.Rotation = transform.rotation.eulerAngles;
            handData.CurrentGridGuid = CurrentGrid.GetInstanceID();
            return handData;
        }
        

        public Slot Insert(Item item)
        {
            var card = item as Card;
            if (card == null)
            {
                throw new Exception("Only cards can be added to hand");
            }

            return Insert(card, grid.Slots.Count);
        }

        private Slot Insert(Card card, int index)
        {
            if (index > grid.Slots.Count || index < 0)
            {
                index = grid.Slots.Count;
            }

            Slot createdSlot = grid.GetFirstEmptySlot();
            if (createdSlot != null)
            {
                //grid.Slots.Insert(index, createdSlot);
                return createdSlot;
            }
            Vector3 position;
            if (grid.Slots.Count == 0)
            {
                position = grid.transform.position - transform.right * 10;
            }
            else if (index == grid.Slots.Count)
            {
                var targetSlot = grid.Slots[index - 1];
                position = targetSlot.transform.position + transform.right * _spacing;
            }
            else
            {
                var targetSlot = grid.Slots[index];
                position = targetSlot.transform.position;
                for (var i = index; i < grid.Slots.Count; i++)
                {
                    grid.Slots[i].transform.position += transform.right * _spacing;
                }
            }
            
            createdSlot = grid.CreateSlot(position);
            grid.Slots.Insert(index, createdSlot);
            return createdSlot;
        }

        private void RemoveCard(Item item, Slot slot)
        {
            Debug.Log("Removing card from hand");
            var card = item as Card;
            if (card == null)
            {
                throw new Exception("Only cards can be removed from Hand");
            }
            // Dont remove last empty slot
            if(grid.Slots.Count == 1) return;;
            
            var slotIndex = grid.Slots.IndexOf(slot);
            // Update position of slots and items that above of slot
            // for (var i = slotIndex; i < grid.Slots.Count; i++)
            // {
            //     grid.Slots[i].transform.position += Vector3.left * _spacing;
            //     grid.Slots[i].Item.transform.position = grid.Slots[i].transform.position; 
            // }
            // Remove empty slot from slot list and destroy it
            slot.Remove();
            // grid.Slots.Remove(slot);
            // Destroy(slot.gameObject);
        }

        // public override MeepleObjectSerializable GetSerializable()
        // {
        //     return new ItemSerializable
        //     {
        //         Guid = GetInstanceID(),
        //         // Prefab = prefab,
        //         Grid = CurrentGrid.GetInstanceID(),
        //         Position = transform.position,
        //         Size = transform.lossyScale,
        //         Rotation = transform.rotation.eulerAngles,
        //         Interactable = false,
        //         Locked = false
        //     };
        // }


        public void AddCard(Card card)
        {
            // var targetSlot = grid.GetFirstEmptySlot();
            // targetSlot.Place(card);
            // card.CurrentSlot?.Remove();
            // card.CurrentSlot = targetSlot;
            // card.Move(targetSlot.Position);
            // card.Flip();
        }

        public void PopCard()
        {
        }

        public void OnSizeChanged()
        {
            // for
        }

        public Grid GetDestination()
        {
            return grid;
        }
    }
}