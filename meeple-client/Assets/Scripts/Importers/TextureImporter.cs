using System;
using System.Collections;
using MeepleClient.Serializables;
using UnityEngine;
using UnityEngine.Networking;

namespace MeepleClient.Importers
{
    public class TextureImporter : MeepleObject
    {
        [SerializeField] private TextureImporterSerializable data;
        [SerializeField, ReadOnly] private Texture2D frontTexture;
        [SerializeField, ReadOnly] private Texture2D backTexture;
        [SerializeField, ReadOnly] private bool frontDownloaded;
        [SerializeField, ReadOnly] private bool backDownloaded;
        private Shader _shader;
        
        public void Initialize(TextureImporterSerializable serializable)
        {
            base.Initialize(serializable);
            _shader = Shader.Find("Standard");
            data = serializable;
            StartCoroutine(DownloadBackTexture());
            StartCoroutine(DownloadFrontTexture());
        }

        public IEnumerator GetMaterials(int i, int j, Action<Material, Material> setMaterials)
        {
            
            while (!frontDownloaded || !backDownloaded)
            {
                yield return null;
            }

            setMaterials(GetFrontMaterial(i, j), GetBackMaterial(i, j));
        }

        // private Material _CreateMaterial(Texture2D bigTexture, int x, int y, int width, int height)
        // {
        //     var pixels = bigTexture.GetPixels(x, y, width, height);
        //     var texture = new Texture2D(width, height);
        //     texture.SetPixels(pixels);
        //     texture.Apply();
        //     return new Material(_shader){mainTexture = texture};
        // }

        private Material CreateCommonMaterial(Texture texture)
        {
            return new Material(_shader){mainTexture = texture};
        }

        private Material CreateUniqueMaterial(Texture2D bigTexture, int i, int j)
        {
            var height = bigTexture.height;
            var width = bigTexture.width;
            var cardHeight = height / data.Row;
            var cardWidth = width / data.Column;

            var number = data.Column * j + i;
            var x = cardWidth * i; 
            var y = (height - cardHeight) - cardHeight * j;
            //return _CreateMaterial(texture, x, y, cardWidth, cardHeight);
            var pixels = bigTexture.GetPixels(x, y, cardWidth, cardHeight);
            var texture = new Texture2D(cardWidth, cardHeight);
            texture.SetPixels(pixels);
            texture.Apply();
            return new Material(_shader){mainTexture = texture};
        }

        private Material GetFrontMaterial(int i, int j)
        {
            return CreateUniqueMaterial(frontTexture, i, j);
        }

        private Material GetBackMaterial(int i, int j)
        {
            if (data.Unique)
            {
                return CreateUniqueMaterial(backTexture, i, j);
            }
            else
            {
                return CreateCommonMaterial(backTexture);
            }
        }

        private IEnumerator DownloadFrontTexture()
        {
            using (UnityWebRequest uwr = UnityWebRequestTexture.GetTexture(data.FrontUrl))
            {
                yield return uwr.SendWebRequest();

                if (uwr.isNetworkError || uwr.isHttpError)
                {
                    Debug.Log(uwr.error);
                }
                else
                {
                    frontTexture = DownloadHandlerTexture.GetContent(uwr);
                    frontDownloaded = true;
                }
            }
        }
        
        private IEnumerator DownloadBackTexture()
        {
            using (UnityWebRequest uwr = UnityWebRequestTexture.GetTexture(data.BackUrl))
            {
                yield return uwr.SendWebRequest();

                if (uwr.isNetworkError || uwr.isHttpError)
                {
                    Debug.Log(uwr.error);
                }
                else
                {
                    backTexture = DownloadHandlerTexture.GetContent(uwr);
                    backDownloaded = true;
                }
            }
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            data.Guid = GetInstanceID();
            data.Name = name;
            return data;
        }
        
        
    }
}