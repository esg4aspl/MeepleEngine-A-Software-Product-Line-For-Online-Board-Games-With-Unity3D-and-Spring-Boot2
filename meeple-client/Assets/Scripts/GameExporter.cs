using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using JsonSubTypes;
using MeepleClient.Serializables;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using UnityEngine;

namespace MeepleClient
{
    public class GameExporter : MonoBehaviour
    {
        [SerializeField] private string path;
        [SerializeField] private GameObject root;

        [SerializeField] private GameSerializable original;

        public void UpdateSerializables()
        {
            original.Objects = new List<MeepleObjectSerializable>();
            AddSerializable(root);
            var settings = new JsonSerializerSettings();
            settings.ContractResolver = new CamelCasePropertyNamesContractResolver();

            settings.Converters.Add(JsonSubtypesConverterBuilder
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
            
            var export = JsonConvert.SerializeObject(original, settings);

            Debug.Log(export);
            File.WriteAllText($@"{path}", export);
        }

        public void AddSerializable(GameObject root)
        {
            GameWorld.ResetMeepleObjects();
            foreach (var componentsInChild in root.GetComponentsInChildren<MeepleObject>())
            {
                Debug.Log(componentsInChild.name);
                var data = componentsInChild.GetSerializable();
                original.Objects.Add(data);
                componentsInChild.Guid = data.Guid;
                GameWorld.AddMeepleObject(componentsInChild);
                
            }
        }
    }
}