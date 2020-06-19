using System;
using Newtonsoft.Json;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class GridSerializable : MeepleObjectSerializable, ICreatable
    {
        [ReadOnly] [SerializeField] private Vector3 position;
        [ReadOnly] [SerializeField] private Vector3 size;
        [ReadOnly] [SerializeField] private Vector3 rotation;
        [ReadOnly] [SerializeField] private int? parentGuid;
        [SerializeField] private int slotLimit;

        public MeepleObject Create(MeepleObject prefab)
        {
            Item parentItem = null;
            var grid = prefab as Grid;
            if (parentGuid != null)
            {
                parentItem = GameWorld.FindMeepleObjectByGuid((int) (parentGuid)) as Item;
                grid = Object.Instantiate(grid, position, Quaternion.Euler(rotation), parentItem.transform);
                // grid = Object.Instantiate(grid, position, Quaternion.Euler(rotation));
            }
            else
            {
                grid = Object.Instantiate(grid, position, Quaternion.Euler(rotation));
            }

            grid.transform.localScale = size;
            grid.Initialize(this, parentItem);
            return grid;
        }

        public Vector3 Position
        {
            get => position;
            set => position = value;
        }

        public Vector3 Size
        {
            get => size;
            set => size = value;
        }

        public Vector3 Rotation
        {
            get => rotation;
            set => rotation = value;
        }

        [JsonProperty(Order = -1)]
        public int? ParentGuid
        {
            get => parentGuid;
            set => parentGuid = value;
        }

        public int SlotLimit
        {
            get => slotLimit;
            set => slotLimit = value;
        }
    }
}