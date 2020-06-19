using System;
using System.Collections.Generic;
using MeepleClient.Importers;
using MeepleClient.Serializables;
using Newtonsoft.Json;
using UnityEngine;

namespace MeepleClient
{
    public class Board : Item
    {
        [SerializeField] private List<Grid> grids = new List<Grid>();
        [SerializeField] private BoardSerializable boardData;
        [SerializeField] private TextureImporter textureImporter;
        [SerializeField] private GameObject holder;
        
        public override void AddGrid(Grid grid)
        {
            grids.Add(grid);
        }

        public BoardSerializable BoardData
        {
            get => boardData;
            set => boardData = value;
        }

        public List<Grid> Grids
        {
            get => grids;
            set => grids = value;
        }
        
        public GameObject Holder
        {
            get => holder;
            set => holder = value;
        }

        public void Initialize(BoardSerializable serializable)
        {
            Debug.Log("Initializing Board");
            base.Initialize(serializable);
            boardData = serializable;
            if (boardData.TextureImporterGuid != null)
            {
                textureImporter = GameWorld.FindMeepleObjectByGuid((int) boardData.TextureImporterGuid) as TextureImporter;
                if (textureImporter is null)
                {
                    throw new Exception("boardTextureImporter is not set properly");
                }

                StartCoroutine(textureImporter.GetMaterials(0, 0, SetMaterials));
            }
            else
            {
                GetComponent<Renderer>().enabled = false;
            }
            // Holder = Instantiate(new GameObject(), Vector3.one, transform.rotation, transform);
        }
        
        private void SetMaterials(Material frontMat, Material backMat)
        {
            var mats = GetComponent<Renderer>().materials;
            mats[0] = frontMat;
            mats[1] = backMat;
            GetComponent<Renderer>().materials = mats;
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            BoardData.Guid = GetInstanceID();
            BoardData.Name = name;
            BoardData.Rotation = transform.rotation.eulerAngles;
            BoardData.Size = transform.localScale;
            BoardData.CurrentGridGuid = CurrentGrid.GetInstanceID();
            BoardData.TextureImporterGuid = textureImporter != null ? textureImporter.GetInstanceID(): (int?) null;
            return BoardData;
        }
    }
}