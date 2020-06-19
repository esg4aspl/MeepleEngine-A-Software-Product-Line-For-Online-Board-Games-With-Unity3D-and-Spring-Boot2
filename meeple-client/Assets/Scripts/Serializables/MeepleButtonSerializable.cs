using System;
using MeepleClient.Core;
using UnityEngine;
using Object = UnityEngine.Object;

namespace MeepleClient.Serializables
{
    [Serializable]
    public class MeepleButtonSerializable: ItemSerializable, ICreatable
    {
        [SerializeField] private string text;
        
        public string Text
        {
            get => text;
            set => text = value;
        }

        public MeepleObject Create(MeepleObject prefab)
        {
            if (!(GameWorld.FindMeepleObjectByGuid(CurrentGridGuid) is Grid parentGrid))
            {
                throw new Exception("parentGrid is not set properly");
            }
            
            var button = prefab as MeepleButton;
            button = Object.Instantiate(button);
            button.Initialize(this);
            new MoveCommand(button, parentGrid).Invoke();
            return button;
        }
    }
}