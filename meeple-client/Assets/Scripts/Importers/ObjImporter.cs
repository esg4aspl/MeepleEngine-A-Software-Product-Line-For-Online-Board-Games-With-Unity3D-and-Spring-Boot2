using System;
using System.Collections;
using MeepleClient.Serializables;
using UnityEngine;
using UnityEngine.Networking;

namespace MeepleClient.Importers
{
    public class ObjImporter : MeepleObject
    {
        [SerializeField] private ObjImporterSerializable data;
        [SerializeField, ReadOnly] private bool meshDownloaded;
        [SerializeField, ReadOnly] private bool diffuseDownloaded;
        [SerializeField, ReadOnly] private Mesh importedMesh;
        [SerializeField, ReadOnly] private Texture2D importedDiffuse;
        [SerializeField, ReadOnly] private LoaderAdapter loaderAdapter;
        private Shader _shader;

        public void Initialize(ObjImporterSerializable serializable)
        {
            base.Initialize(serializable);
            _shader = Shader.Find("Standard");
            data = serializable;
            DownloadMesh();
            StartCoroutine(DownloadDiffuse());
        }

        public IEnumerator GetMesh(Action<Mesh> setMesh)
        {
            yield return new WaitUntil(() => meshDownloaded);
            setMesh(importedMesh);
        }

        public IEnumerator GetMaterial(Action<Material> setMaterial)
        {
            yield return new WaitUntil(() => diffuseDownloaded);
            setMaterial(new Material(_shader){mainTexture = importedDiffuse});
        }
        
        public override MeepleObjectSerializable GetSerializable()
        {
            data.Guid = GetInstanceID();
            data.Name = name;
            return data;
        }

        private void DownloadMesh()
        {
            loaderAdapter.GetMesh(data.ModelUrl, mesh =>
            {
                Debug.Log("ImportedMesh = mesh");
                importedMesh = mesh;
                meshDownloaded = true;
            });
        }
        
        private IEnumerator DownloadDiffuse()
        {
            using (UnityWebRequest uwr = UnityWebRequestTexture.GetTexture(data.DiffuseUrl))
            {
                yield return uwr.SendWebRequest();

                if (uwr.isNetworkError || uwr.isHttpError)
                {
                    Debug.Log(uwr.error);
                }
                else
                {
                    importedDiffuse = DownloadHandlerTexture.GetContent(uwr);
                    diffuseDownloaded = true;
                }
            }
        }
    }
}