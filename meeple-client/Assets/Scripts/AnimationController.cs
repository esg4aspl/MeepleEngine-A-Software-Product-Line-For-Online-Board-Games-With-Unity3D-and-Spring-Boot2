using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace MeepleClient
{
    public class AnimationController : MonoBehaviour
    {
        [SerializeField] private Queue<IEnumerator> animationQueue = new Queue<IEnumerator>();
        
        // Move settings
        
        // Flip settings
        public float liftAmount = 1f;
        public float liftDuration = 0.3f;
        public float rotateDuration = 0.3f;
        public bool isAnimating = false;

        public Collider Collider;

        public void Awake()
        {
            Collider = GetComponent<Collider>();
            if(Collider != null)
                liftAmount = Collider.bounds.size.x / 2;
        }
        
        private void FixedUpdate()
        {
            if (animationQueue.Count == 0) return;
            if (isAnimating) return;
            StartCoroutine(animationQueue.Dequeue());
            isAnimating = true;
        }

        public void Flip()
        {
            animationQueue.Enqueue(FlipEnumerator());
        }

        public void Move(Vector3 position, Quaternion rotation)
        {
            animationQueue.Enqueue(MoveEnumerator(position, rotation));
        }

        private IEnumerator FlipEnumerator()
        {
            var seq = LeanTween.sequence();
            seq.append(LeanTween.moveLocalY(gameObject, transform.localPosition.y + liftAmount, liftDuration)
                .setEaseOutQuad());
            seq.append(LeanTween.rotateAround(gameObject, transform.forward, 180, rotateDuration));
            seq.append(LeanTween.moveLocalY(gameObject, transform.localPosition.y, liftDuration).setEaseOutQuad());
            seq.append(() =>
            {
                isAnimating = false;
            });
            yield return null;
            
        }

        private IEnumerator MoveEnumerator(Vector3 position, Quaternion rotation)
        {
            LeanTween.init(1200);
            var seq = LeanTween.sequence();
            seq.append(LeanTween.moveLocalY(gameObject, transform.localPosition.y + liftAmount, 0.5f).setEaseOutQuad());
            seq.append(LeanTween.move(gameObject, position, 0.5f).setEaseOutQuad());
            LeanTween.rotateX(gameObject, rotation.eulerAngles.x, 0.5f);
            LeanTween.rotateY(gameObject, rotation.eulerAngles.y, 0.5f);
            seq.append(() =>
            {
                isAnimating = false;
            });
            yield return null;
        }


    }
}