using System;
using MeepleClient.Importers;
using Newtonsoft.Json;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [JsonObject(MemberSerialization.OptIn)]
    [Serializable]
    public class TextureImporterSerializable : MeepleObjectSerializable, ICreatable
    {
        [JsonProperty] [SerializeField] private string frontUrl;
        [JsonProperty] [SerializeField] private string backUrl;
        [JsonProperty] [SerializeField] private int row;
        [JsonProperty] [SerializeField] private int column;
        [SerializeField] private bool unique;

        public MeepleObject Create(MeepleObject prefab)
        {
            var textureImporter = prefab as TextureImporter;
            textureImporter = Object.Instantiate(textureImporter);
            textureImporter.Initialize(this);
            return textureImporter;
        }
        
        public string FrontUrl
        {
            get => frontUrl;
            set => frontUrl = value;
        }

        public string BackUrl
        {
            get => backUrl;
            set => backUrl = value;
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

        public bool Unique
        {
            get => unique;
            set => unique = value;
        }
    }
}