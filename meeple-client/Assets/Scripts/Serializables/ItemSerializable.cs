using System;
using RotaryHeart.Lib.SerializableDictionary;
using UnityEngine;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class DataDict : SerializableDictionaryBase<string, string>
    {
    }

    [Serializable]
    public class ItemSerializable : MeepleObjectSerializable
    {
        [ReadOnly] [SerializeField] private int currentGridGuid; // Position
        [ReadOnly] [SerializeField] private Vector3 size;
        [ReadOnly] [SerializeField] private Vector3 rotation;
        [SerializeField] private DataDict data;


        // public GameObject Prefab
        // {
        //     get => prefab;
        //     set => prefab = value;
        // }

        public int CurrentGridGuid
        {
            get => currentGridGuid;
            set => currentGridGuid = value;
        }

        // public Vector3 Position
        // {
        //     get => position;
        //     set => position = value;
        // }

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
        
        public DataDict Data
        {
            get => data;
            set => data = value;
        }
    }
}