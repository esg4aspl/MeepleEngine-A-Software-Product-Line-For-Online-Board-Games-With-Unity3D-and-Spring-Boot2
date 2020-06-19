using System;
using System.Collections.Generic;
using UnityEngine;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class GameSerializable
    {
        [SerializeField] private List<MeepleObjectSerializable> objects = new List<MeepleObjectSerializable>();

        public List<MeepleObjectSerializable> Objects
        {
            get => objects;
            set => objects = value;
        }
    }
}