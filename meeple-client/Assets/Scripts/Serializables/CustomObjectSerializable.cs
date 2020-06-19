using System;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class CustomObjectSerializable: ItemSerializable, ICreatable
    {
        [SerializeField, ReadOnly] private int objImporterGuid;

        public int ObjImporterGuid
        {
            get => objImporterGuid;
            set => objImporterGuid = value;
        }

        public MeepleObject Create(MeepleObject prefab)
        {
            if (!(GameWorld.FindMeepleObjectByGuid(CurrentGridGuid) is Grid parentGrid))
            {
                throw new Exception("parentGrid is not set properly");
            }

            Debug.Log("Creating card");
            var customObject = prefab as CustomObject;
            // card = Object.Instantiate(card, parentGrid.transform);
            customObject = Object.Instantiate(customObject);
            customObject.Initialize(this);
            new MoveCommand(customObject, parentGrid, false).Invoke();
            return customObject;
        }
    }
}