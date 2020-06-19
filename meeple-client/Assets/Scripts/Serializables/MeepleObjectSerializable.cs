using System;
using Newtonsoft.Json;
using UnityEngine;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class MeepleObjectSerializable
    {
        [ReadOnly] [SerializeField] private int guid;
        [ReadOnly] [SerializeField] private string name;
        [SerializeField] private bool interactable;

        [JsonProperty(Order = -1)]
        public int Guid
        {
            get => guid;
            set => guid = value;
        }

        [JsonProperty(Order = -2)]
        public string Name
        {
            get => name;
            set => name = value;
        }

        public bool Interactable
        {
            get => interactable;
            set => interactable = value;
        }
    }
}