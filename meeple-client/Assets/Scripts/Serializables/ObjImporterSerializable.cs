using System;
using MeepleClient.Importers;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class ObjImporterSerializable : MeepleObjectSerializable, ICreatable
    {
        [SerializeField] private string modelUrl;
        [SerializeField] private string diffuseUrl;

        public string ModelUrl
        {
            get => modelUrl;
            set => modelUrl = value;
        }

        public string DiffuseUrl
        {
            get => diffuseUrl;
            set => diffuseUrl = value;
        }

        public MeepleObject Create(MeepleObject prefab)
        {
            var objImporter = prefab as ObjImporter;
            objImporter = Object.Instantiate(objImporter);
            objImporter.Initialize(this);
            return objImporter;
        }
    }
}