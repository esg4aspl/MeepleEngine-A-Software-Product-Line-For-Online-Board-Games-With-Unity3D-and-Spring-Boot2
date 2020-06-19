using System;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class HandSerializable: ItemSerializable, ICreatable
    {
        [SerializeField] private float spacing;
        [SerializeField] private bool active;

        public float Spacing
        {
            get => spacing;
            set => spacing = value;
        }

        public bool Active
        {
            get => active;
            set => active = value;
        }

        public MeepleObject Create(MeepleObject prefab)
        {
            if (!(GameWorld.FindMeepleObjectByGuid(CurrentGridGuid) is Grid parentGrid))
            {
                throw new Exception("parentGrid is not set properly");
            }
            
            var hand = prefab as Hand;
            hand = Object.Instantiate(hand, parentGrid.transform);
            // deck = Object.Instantiate(deck);
            hand.Initialize(this);
            new MoveCommand(hand, parentGrid, false).Invoke();
            return hand;
        }
    }
}