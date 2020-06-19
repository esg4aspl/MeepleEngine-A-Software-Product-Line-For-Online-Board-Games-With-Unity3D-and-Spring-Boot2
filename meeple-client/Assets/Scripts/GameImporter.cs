using System;
using System.Collections.Generic;
using JsonSubTypes;
using MeepleClient.Core;
using MeepleClient.Importers;
using MeepleClient.Serializables;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using RotaryHeart.Lib.SerializableDictionary;
using UnityEngine;
using UnityEngine.Assertions;

namespace MeepleClient
{
    [Serializable]
    public class TypePrefabs : SerializableDictionaryBase<string, MeepleObject>
    {
        public MeepleObject Get(Type type)
        {
            return this[type.ToString()];
        }
    }
    
    
    public class GameImporter: MonoBehaviour
    {
        [SerializeField] private TypePrefabs prefabs;
        
        [SerializeField] private GameSerializable gameSerializable;

        [SerializeField] private string json;
        [SerializeField] private TextAsset jsonFile;
        [SerializeField] private string path;

        [SerializeField] private Board boardPrefab;
        [SerializeField] private Card cardPrefab;
        [SerializeField] private Grid gridPrefab;
        [SerializeField] private TextureImporter textureImporterPrefab;
        [SerializeField] private ObjImporter objImporterPrefab;
        [SerializeField] private Deck deckPrefab;
        [SerializeField] private MeepleButton buttonPrefab;
        [SerializeField] private CustomObject customObjectPrefab;
        [SerializeField] private Hand handPrefab;
        [SerializeField] private GameWorld.IdObject objects;
        private JsonSerializerSettings _serializerSettings;
        
        
        private void Awake()
        {
            _serializerSettings = new JsonSerializerSettings();
            _serializerSettings.Converters.Add(JsonSubtypesConverterBuilder
                .Of(typeof(MeepleObjectSerializable), "type") // type property is only defined here
                .RegisterSubtype(typeof(CardSerializable), "card")
                .RegisterSubtype(typeof(GridSerializable), "grid")
                .RegisterSubtype(typeof(BoardSerializable), "board")
                .RegisterSubtype(typeof(TextureImporterSerializable), "cardImporter")
                .RegisterSubtype(typeof(ObjImporterSerializable), "objImporter")
                .RegisterSubtype(typeof(DeckSerializable), "deck")
                .RegisterSubtype(typeof(MeepleButtonSerializable), "meepleButton")
                .RegisterSubtype(typeof(CustomObjectSerializable), "customObject")
                .RegisterSubtype(typeof(HandSerializable), "hand")
                .SerializeDiscriminatorProperty() // ask to serialize the type property
                .Build());
            _serializerSettings.ContractResolver = new CamelCasePropertyNamesContractResolver();
            
            prefabs.Add(typeof(CardSerializable).ToString(), cardPrefab);
            prefabs.Add(typeof(GridSerializable).ToString(), gridPrefab);
            prefabs.Add(typeof(BoardSerializable).ToString(), boardPrefab);
            prefabs.Add(typeof(DeckSerializable).ToString(), deckPrefab);
            prefabs.Add(typeof(TextureImporterSerializable).ToString(), textureImporterPrefab);
            prefabs.Add(typeof(ObjImporterSerializable).ToString(), objImporterPrefab);
            prefabs.Add(typeof(MeepleButtonSerializable).ToString(), buttonPrefab);
            prefabs.Add(typeof(CustomObjectSerializable).ToString(), customObjectPrefab);
            prefabs.Add(typeof(HandSerializable).ToString(), handPrefab);
        }

        public void Start()
        {
            json = jsonFile.text;
            var result = JsonConvert.DeserializeObject<GameSerializable>(json, _serializerSettings);
            foreach (var meepleObject in result.Objects)
            {
                Debug.Log(meepleObject.GetType());
                if (meepleObject is ICreatable creatable)
                {
                    var obj = creatable.Create(prefabs.Get(meepleObject.GetType()));
                    GameWorld.AddMeepleObject(obj);
                }
            }
        }
    }
}