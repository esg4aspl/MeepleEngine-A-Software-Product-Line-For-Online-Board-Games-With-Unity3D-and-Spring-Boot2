using System;
using RotaryHeart.Lib.SerializableDictionary;
using UnityEngine;

namespace MeepleClient
{
    public static class GameWorld
    {
        public static IdObject MeepleObjects { get; set; } = new IdObject();

        public static MeepleObject FindMeepleObjectByGuid(int guid)
        {
            return MeepleObjects[guid];
        }

        public static MeepleObject FindMeepleObjectByGuid(string guid)
        {
            return FindMeepleObjectByGuid(int.Parse(guid));
        }

        public static void AddMeepleObject(MeepleObject meepleObject)
        {
            Debug.Log("Added object: " + meepleObject.Guid + " "+ meepleObject.name);
            MeepleObjects.Add(meepleObject.Guid, meepleObject);
        }

        public static void ResetMeepleObjects()
        {
            MeepleObjects = new IdObject();
        }

        [Serializable]
        public class IdObject : SerializableDictionaryBase<int, MeepleObject>
        {
        }
    }
}