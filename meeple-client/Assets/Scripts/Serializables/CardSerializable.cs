using System;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class CardSerializable : ItemSerializable, ICreatable
    {
        [SerializeField, ReadOnly] private int cardImporterGuid;
        [SerializeField] private int row;
        [SerializeField] private int column;
        [SerializeField] private float thickness;

        public MeepleObject Create(MeepleObject prefab)
        {
            if (!(GameWorld.FindMeepleObjectByGuid(CurrentGridGuid) is Grid parentGrid))
            {
                throw new Exception("parentGrid is not set properly");
            }
            
            var card = prefab as Card;
            // card = Object.Instantiate(card, parentGrid.transform);
            card = Object.Instantiate(card);
            card.Initialize(this);
            new MoveCommand(card, parentGrid).Invoke();
            return card;
        }

        public float Thickness
        {
            get => thickness;
            set => thickness = value;
        }

        public int CardImporterGuid
        {
            get => cardImporterGuid;
            set => cardImporterGuid = value;
        }

        public int Row
        {
            get => row;
            set => row = value;
        }

        public int Column
        {
            get => column;
            set => column = value;
        }
    }
}