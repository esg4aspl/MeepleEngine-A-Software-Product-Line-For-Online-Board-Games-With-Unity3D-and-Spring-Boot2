using MeepleClient.Serializables;
using UnityEngine;

namespace MeepleClient
{
    // [RequireComponent(typeof(FlipController))]
    public abstract class Item : MeepleObject
    {
        public AnimationController _animationController;
        [SerializeField] private Grid currentGrid;
        // [SerializeField] private Grid ownGrid;
        
        
        public Grid CurrentGrid
        {
            get => currentGrid;
            set => currentGrid = value;
        }

        // public Grid OwnGrid
        // {
        //     get => ownGrid;
        //     set => ownGrid = value;
        // }

        public abstract void AddGrid(Grid grid);
        
        public abstract override MeepleObjectSerializable GetSerializable();

        public void Initialize(ItemSerializable serializable)
        {
            base.Initialize(serializable);
            transform.localScale = serializable.Size;
            transform.rotation = Quaternion.Euler(serializable.Rotation);
            
        }


        protected virtual void Awake()
        {
            _animationController = GetComponent<AnimationController>();
        }

        public virtual void AnimateFlip()
        {
            Debug.Log("Flip controller will be called");
            _animationController.Flip();
        }

        public void Rotate()
        {
            LeanTween.moveLocalY(gameObject, 1.6f, 1f).setEaseOutQuad();
            LeanTween.rotateAround(gameObject, transform.up, 15, 1f);
        }

        public void AnimateMove(Vector3 position, Quaternion rotation)
        {
            _animationController.Move(position, rotation);
        }
    }
}