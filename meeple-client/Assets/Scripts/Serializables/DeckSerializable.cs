using System;
using UnityEngine;
using Object = UnityEngine.Object;


namespace MeepleClient.Serializables
{
    [Serializable]
    public class DeckSerializable : ItemSerializable, ICreatable
    {
        public MeepleObject Create(MeepleObject prefab)
        {
            if (!(GameWorld.FindMeepleObjectByGuid(CurrentGridGuid) is Grid parentGrid))
            {
                throw new Exception("parentGrid is not set properly");
            }
            
            var deck = prefab as Deck;
            deck = Object.Instantiate(deck, parentGrid.transform);
            // deck = Object.Instantiate(deck);
            deck.Initialize(this);
            new MoveCommand(deck, parentGrid, false).Invoke();
            return deck;
        }
    }
}