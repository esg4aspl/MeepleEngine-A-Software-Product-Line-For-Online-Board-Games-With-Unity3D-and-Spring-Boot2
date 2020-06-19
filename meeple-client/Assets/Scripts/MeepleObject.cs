using MeepleClient.Serializables;
using UnityEngine;

namespace MeepleClient
{
    public abstract class MeepleObject : MonoBehaviour
    {
        [SerializeField] private int guid;

        public abstract MeepleObjectSerializable GetSerializable();

        public void Initialize(MeepleObjectSerializable serializable)
        {
            guid = serializable.Guid;
            name = serializable.Name;
            if (!serializable.Interactable)
            {
                var collider = GetComponent<Collider>();
                if (collider != null)
                {
                    collider.enabled = false;
                }
            }
        }
        
        public int Guid
        {
            get => guid;
            set => guid = value;
        }
    }
}