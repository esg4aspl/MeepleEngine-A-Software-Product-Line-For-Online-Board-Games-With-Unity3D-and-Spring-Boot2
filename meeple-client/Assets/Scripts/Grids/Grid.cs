using System;
using System.Collections.Generic;
using System.Linq;
using MeepleClient.Serializables;
using Newtonsoft.Json;
using UnityEngine;

namespace MeepleClient
{
    [JsonObject(MemberSerialization.OptIn)]
    public class Grid : MeepleObject, IPlaceable
    {
        // [SerializeField] private long id;
        [SerializeField] protected Item parent;
        [SerializeField] protected List<Slot> slots;
        [SerializeField] protected Slot slotPrefab;
        [SerializeField] protected bool interactable;
        [JsonProperty]
        [SerializeField] private GridSerializable gridData;
        
        public event Func<Item, Slot> OnInsert;
        public event Action<Item, Slot> OnRemove;

        private void Awake()
        {
            slots = new List<Slot>();
        }

        public List<Slot> Slots => slots;

        public GridSerializable GridData
        {
            get => gridData;
            set => gridData = value;
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            GridData.Guid = GetInstanceID();
            GridData.Name = name;
            GridData.Position = transform.position;
            GridData.Rotation = transform.rotation.eulerAngles;
            GridData.Size = transform.localScale;
            GridData.ParentGuid = parent != null ? parent.GetInstanceID() : (int?) null;
            return GridData;
        }

        public void Initialize(GridSerializable gridSerializable, Item parentItem = null)
        {
            Debug.Log("Initializing Grid");
            base.Initialize(gridSerializable);
            gridData = gridSerializable;
            parent = parentItem;
            if (parentItem != null)
            {
                parent.AddGrid(this);
            }

        }

        public Slot CreateSlot(Vector3 position)
        {
            var slot = Instantiate(slotPrefab, position, transform.rotation, transform);
            // slot.transform.localScale = Vector3.one;
            return slot;
        }

        public void Initialize(Item parentMeepleObject = null)
        {
            Debug.Log("Infinite grid initializing");
            parent = parentMeepleObject;
            slots = new List<Slot>();
            // var createdSlot = Instantiate(slotPrefab, transform.position, transform.rotation, transform);
            // slots.Add(createdSlot);
            Debug.Log("grid initialized");
        }

        public void Initialize(int slotLimit, Item parentMeepleObject = null)
        {
            parent = parentMeepleObject;
            slots = new List<Slot>();
            var xSpacing = transform.lossyScale.x / slotLimit;
            var multiplier = -1;
            var translate = 0f;
            if (slotLimit % 2 == 0)
            {
                translate = -xSpacing / 2;
            }

            for (var i = 0; i < slotLimit; i++)
            {
                var createdSlot = Instantiate(slotPrefab,
                    transform.position + transform.right * (Mathf.Ceil(i / 2f) * multiplier * xSpacing + translate),
                    transform.rotation, transform);
                multiplier *= -1;
                slots.Add(createdSlot);
            }
        }

        public void Remove(Item item)
        {
            var itemSlot = slots.FirstOrDefault(slot => slot.Item == item);
            if (itemSlot == null) return;
            itemSlot.Remove();
            OnRemove?.Invoke(item, itemSlot);
        }

        public Slot GetAvailableSlot(Item item)
        {
            Slot slot;
            if (OnInsert != null)
            {
                slot = OnInsert(item);
            }
            else
            {
                slot = GetFirstEmptySlot();
                if (slot == null)
                {
                    var createdSlot = Instantiate(slotPrefab, transform.position, transform.rotation, transform);
                    slots.Add(createdSlot);
                    slot = createdSlot;
                }
            }

            return slot;
        }

        public Slot GetLastSlotThatHaveItem()
        {
            return slots.Last(slot => !slot.IsEmpty());
        }

        public Slot GetFirstEmptySlot()
        {
            return slots.FirstOrDefault(slot => slot.IsEmpty());
        }

        public Grid GetDestination()
        {
            return this;
        }

        void OnDrawGizmos()
        {
            // Draw a yellow sphere at the transform's position
            Gizmos.color = Color.blue;
            // Gizmos.DrawSphere(transform.position, .1f);
            Gizmos.DrawCube(transform.position, transform.lossyScale);
        }
    }
}