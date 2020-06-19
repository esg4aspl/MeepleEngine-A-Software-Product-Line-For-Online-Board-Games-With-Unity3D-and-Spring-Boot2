using System;
using MeepleClient.Importers;
using MeepleClient.Serializables;
using UnityEngine;

namespace MeepleClient
{
    public class CustomObject: Item
    {
        [SerializeField] private CustomObjectSerializable data;
        [SerializeField] private ObjImporter objImporter;
        [SerializeField, ReadOnly] private MeshFilter meshFilter;
        
        public void Initialize(CustomObjectSerializable serializable)
        {
            base.Initialize(serializable);
            data = serializable;
            objImporter = GameWorld.FindMeepleObjectByGuid(data.ObjImporterGuid) as ObjImporter;
            if (objImporter is null)
            {
                throw new Exception("objImporter is not set properly");
            }
            SetStartVisuals();
        }

        private void SetStartVisuals()
        {
            StartCoroutine(objImporter.GetMaterial(SetMaterial));
            StartCoroutine(objImporter.GetMesh(SetMesh));
        }
        private void SetMaterial(Material diffuseMat)
        {
            var mats = GetComponent<Renderer>().materials;
            mats[0] = diffuseMat;
            GetComponent<Renderer>().materials = mats;
        }

        private void SetMesh(Mesh mesh)
        {
            Debug.Log("meshFilter.mesh");
            meshFilter.mesh = mesh;
        }
        
        public override void AddGrid(Grid grid)
        {
            throw new System.NotImplementedException();
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            data.Guid = GetInstanceID();
            data.Name = gameObject.name;
            data.Size = transform.localScale;
            data.Rotation = transform.rotation.eulerAngles;
            data.CurrentGridGuid = CurrentGrid.GetInstanceID();
            data.ObjImporterGuid = objImporter.GetInstanceID();
            return data;
        }
    }
}