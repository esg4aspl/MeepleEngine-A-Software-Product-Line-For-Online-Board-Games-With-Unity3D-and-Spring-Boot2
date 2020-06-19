using System;
using AsImpL;
using UnityEngine;

namespace MeepleClient.Importers
{
    public class LoaderAdapter : ObjectImporter
    {
        private PathSettings _pathSettings;
        [SerializeField] private ImportOptions _defaultImportOptions = new ImportOptions();
        private Action<Mesh> setMesh;
        private void Awake()
        {
            _pathSettings = gameObject.AddComponent<PathSettings>();
            _pathSettings.defaultRootPath = RootPathEnum.Url;
            _pathSettings.defaultRootPath = RootPathEnum.Url;
            _defaultImportOptions.zUp = false;
        }

        public void GetMesh(string url, Action<Mesh> onComplete)
        {
            var loader  = ImportModelAsync("importedObject", _pathSettings.RootPath + url, transform, _defaultImportOptions);
            loader.ModelLoaded += OnModelLoaded;
            setMesh = onComplete;
        }

        private void OnModelLoaded(GameObject obj, string path)
        {
            Debug.Log("Model Loaded");
            obj.SetActive(false);
            setMesh(obj.transform.GetComponentInChildren<MeshFilter>().mesh);
        }
    }
}