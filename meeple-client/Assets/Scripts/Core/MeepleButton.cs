using System;
using MeepleClient.Serializables;
using TMPro;
using UnityEngine;

namespace MeepleClient.Core
{
    public class MeepleButton : Item
    {
        [SerializeField] private MeepleButtonSerializable buttonData;
        // [SerializeField, ReadOnly] private string text;
        [SerializeField, ReadOnly] private TextMeshPro textMeshPro;
        [SerializeField, ReadOnly] private Transform cube;
        [SerializeField, ReadOnly] private BoxCollider collider;

        public override void AddGrid(Grid grid)
        {
            throw new NotImplementedException();
        }

        public override MeepleObjectSerializable GetSerializable()
        {
            buttonData.Guid = GetInstanceID();
            buttonData.Name = name;
            buttonData.Rotation = transform.rotation.eulerAngles;
            buttonData.Size = transform.localScale;
            buttonData.Text = name;
            buttonData.CurrentGridGuid = CurrentGrid.GetInstanceID();
            return buttonData;
        }
        

        public void Initialize(MeepleButtonSerializable serializable)
        {
            base.Initialize(serializable);
            buttonData = serializable;
            textMeshPro.text = serializable.Text;
            collider.size = cube.localScale;
        }

        private void FixedUpdate()
        {
            textMeshPro.enabled = true;
            collider.size = cube.localScale;
        }
    }
}