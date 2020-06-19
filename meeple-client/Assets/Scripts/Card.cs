using System;
using System.Collections;
using System.Linq;
using MeepleClient.Importers;
using MeepleClient.Serializables;
using Newtonsoft.Json;
using UnityEngine;
using UnityEngine.Networking;

namespace MeepleClient
{

    public class Card : Item
    {
        [SerializeField] private CardSerializable cardData;
        [SerializeField] private TextureImporter textureImporter;
        private Grid _grid;
        
        private float _thickness;
        private float _aspectRatio;
        

        public void Initialize(CardSerializable cardSerializable)
        {
            base.Initialize(cardSerializable);
            cardData = cardSerializable;
            _thickness = cardData.Thickness;
            textureImporter = GameWorld.FindMeepleObjectByGuid(cardSerializable.CardImporterGuid) as TextureImporter;
            if (textureImporter is null)
            {
                throw new Exception("cardImporter is not set properly");
            }

            StartSetMaterials();
        }

        // For external calls
        private void StartSetMaterials()
        {
            StartCoroutine(textureImporter.GetMaterials(cardData.Column, cardData.Row, SetMaterials));
        }
        
        // Is given parameter to get materials coroutine of card importer, when all texture download and parsing texture operations finished
        // SetMaterials will be called 
        private void SetMaterials(Material frontMat, Material backMat)
        {
            var mats = GetComponent<Renderer>().materials;
            mats[0] = frontMat;
            mats[1] = backMat;
            GetComponent<Renderer>().materials = mats;
        }


        public override void AddGrid(Grid newGrid)
        {
            _grid = newGrid;
            _grid.transform.localPosition = Vector3.up * 0.03f;
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            cardData.Guid = GetInstanceID();
            cardData.Name = name;
            cardData.Rotation = transform.rotation.eulerAngles;
            cardData.Size = transform.localScale;
            cardData.CurrentGridGuid = CurrentGrid.GetInstanceID();
            cardData.CardImporterGuid = textureImporter.GetInstanceID();
            return cardData;
        }

        public void UpdateTexture()
        {
            StartSetMaterials();
        }
        
        public float Thickness
        {
            get => _thickness;
            set => _thickness = value;
        }

        public CardSerializable CardData
        {
            get => cardData;
            set => cardData = value;
        }
    }
}