using System.Collections.Generic;
using System.IO;
using AsImpL.Examples;
using UnityEngine;
using UnityEngine.UI;

namespace AsImpL
{
    /// <summary>
    /// Read a json configuration file and load the object listed there with their parameters and positions.
    /// </summary>
    public class JsonObjImporter : MultiObjectImporter
    {
        [Tooltip("Text for displaying the overall scale")]
        public Text objScalingText;
        
        [Tooltip("Configuration XML file (relative to the application data folder)")]
        public string configFile = "../object_list.json";

        private List<ModelImportInfo> modelsToImport = new List<ModelImportInfo>();

        private void Awake()
        {
            configFile = RootPath + configFile;
        }

        protected override void Start()
        {
            if (autoLoadOnStart)
            {
                Reload();
            }
        }

        public void Reload()
        {
            if (string.IsNullOrEmpty(configFile))
            {
                Debug.LogWarning("Empty configuration file path");
                return;
            }

            try
            {
                // var a =  new ModelImportInfoWrapper();
                // a.modelImportInfo = new List<ModelImportInfo>();
                // var item = new ModelImportInfo();
                // item.name = "Refl";
                // item.path = "models/OBJ_test/testRefl.obj";
                // item.skip = false;
                // a.modelImportInfo.Add(item);
                // a.modelImportInfo.Add(new ModelImportInfo(){name = "BumbRefl", path = "models/OBJ_test/testRefl.obj", skip = false});
                // // Debug.Log(JsonUtility.ToJson(a));
                Debug.Log(Application.dataPath);
                Debug.Log(configFile);
                var jsonString = File.ReadAllText(configFile);
                objectsList = JsonUtility.FromJson<ModelImportInfoWrapper>(jsonString).modelImportInfo;
                // objectsList = (List<ModelImportInfo>) serializer.Deserialize(stream);
                UpdateScene();
                ImportModelListAsync(modelsToImport.ToArray());
            }
            catch (IOException e)
            {
                Debug.LogWarningFormat("Unable to open configuration file {0}: {1}", configFile, e);
            }
        }

        private void UpdateObject(GameObject gameObj, ModelImportInfo importInfo)
        {
            gameObj.name = importInfo.name;
            //game_object.transform.localScale = scale;
            if (importInfo.loaderOptions != null)
            {
                gameObj.transform.localPosition = importInfo.loaderOptions.localPosition;
                gameObj.transform.localRotation = Quaternion.Euler(importInfo.loaderOptions.localEulerAngles);
                gameObj.transform.localScale = importInfo.loaderOptions.localScale;
            }
        }


        private void UpdateImportInfo(ModelImportInfo importInfo, GameObject gameObj)
        {
            importInfo.name = gameObj.name;
            if (importInfo.loaderOptions == null)
            {
                importInfo.loaderOptions = new ImportOptions();
            }

            importInfo.loaderOptions.localPosition = gameObj.transform.localPosition;
            importInfo.loaderOptions.localEulerAngles = gameObj.transform.localRotation.eulerAngles;
            importInfo.loaderOptions.localScale = gameObj.transform.localScale;
        }


        private void UpdateObjectList()
        {
            for (int i = 0; i < transform.childCount; i++)
            {
                Transform tr = transform.GetChild(i);
                ModelImportInfo info = objectsList.Find(obj => obj.name == tr.name);
                if (info != null)
                {
                    UpdateImportInfo(info, tr.gameObject);
                }
            }
        }


        private void UpdateScene()
        {
            modelsToImport.Clear();
            List<string> names = new List<string>();
            // add or update objects that are present in the list
            foreach (ModelImportInfo info in objectsList)
            {
                names.Add(info.name);
                Transform transf = transform.Find(info.name);
                if (transf == null)
                {
                    modelsToImport.Add(info);
                }
                else
                {
                    UpdateObject(transf.gameObject, info);
                }
            }

            // destroy objects that are not present in the list
            for (int i = 0; i < transform.childCount; i++)
            {
                Transform tr = transform.GetChild(i);
                if (tr.gameObject != gameObject && !names.Contains(tr.gameObject.name))
                {
                    Destroy(tr.gameObject);
                }
            }
        }
    }
}