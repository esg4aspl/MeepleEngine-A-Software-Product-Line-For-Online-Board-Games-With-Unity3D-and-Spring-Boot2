using System.Collections.Generic;

namespace AsImpL
{
    /// <summary>
    /// Model import Info Wrapper, used for serializing JSON 
    /// </summary>
    [System.Serializable]
    public class ModelImportInfoWrapper
    {
        public List<ModelImportInfo> modelImportInfo;
    }
}