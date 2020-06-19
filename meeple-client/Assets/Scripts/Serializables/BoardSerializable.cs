using System;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class BoardSerializable : ItemSerializable, ICreatable
    {
        [SerializeField, ReadOnly] private int? textureImporterGuid;

        
        public int? TextureImporterGuid
        {
            get => textureImporterGuid;
            set => textureImporterGuid = value;
        }
        

        public MeepleObject Create(MeepleObject prefab)
        {
            if (!(GameWorld.FindMeepleObjectByGuid(CurrentGridGuid) is Grid parentGrid))
            {
                throw new Exception("parentGrid is not set properly");
            }

            Debug.Log("Creating board");
            var board = prefab as Board;
            board = Object.Instantiate(board, parentGrid.transform);
            // board = Object.Instantiate(board);
            board.Initialize(this);
            new MoveCommand(board, parentGrid, false).Invoke();
            return board;
        }
    }
}