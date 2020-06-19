using System;
using UnityEngine;

namespace MeepleClient
{
    public class Slot : MonoBehaviour
    {
        [SerializeField] private Item item;

        public bool IsEmpty()
        {
            return item == null;
        }

        public Vector3 Position
        {
            get => transform.position;
        }

        public Quaternion Rotation
        {
            get => transform.rotation;
        }

        public Item Item
        {
            get => item;
            set => item = value;
        }

        public void Place(Item item)
        {
            if (IsEmpty())
            {
                this.item = item;
            }
            else
            {
                throw new Exception("This slot already filled");
            }
        }

        public Item Remove()
        {
            var temp = item;
            item = null;
            return temp;
        }

        void OnDrawGizmos()
        {
            // Draw a yellow sphere at the transform's position
            Gizmos.color = Color.red;
            Gizmos.DrawSphere(transform.position, .2f);
        }
    }
}