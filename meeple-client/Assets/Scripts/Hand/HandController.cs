using System;
using UnityEngine;

namespace MeepleClient
{
    public class HandController : MonoBehaviour
    {
        [SerializeField] public RectTransform playerHand;
        [SerializeField] private Camera handCamera;
        [SerializeField] private RenderTexture playerHandTexture;
        public bool isOnHand;
        
        [SerializeField, ReadOnly] private Vector3 _relativePosition;
        [SerializeField, ReadOnly] private Vector3[] corners = new Vector3[4];
        
        private void Update()
        {
            isOnHand = IsOnHand();
        }

        public bool IsOnHand()
        {
            playerHand.GetWorldCorners(corners);
            var width = playerHand.rect.width;
            var height = playerHand.rect.height;
            _relativePosition = Input.mousePosition - corners[0];
            var x = _relativePosition.x;
            var y = _relativePosition.y;
            //
            var camHeight = handCamera.pixelHeight;
            var camWidth = handCamera.pixelWidth;
            
            var wMul = camWidth / width;
            var hMul = camHeight / height;
            _relativePosition.Scale(new Vector3(wMul, hMul, 1));
            return x > 0 && y > 0 && x < width && y < height;
        }

        public T Click<T>(LayerMask layerMask) where T : MeepleObject
        {
            T result = null;
            var ray = handCamera.ScreenPointToRay(_relativePosition);
            Debug.DrawRay(ray.origin, ray.direction * 100, Color.blue, 1);
            if (Physics.Raycast(ray, out var hit, 100, layerMask))
            {
                // Debug.DrawLine(ray.origin, ray.origin + ray.direction * hit.distance);
                result = hit.transform.gameObject.GetComponent<T>();
                if (result is null)
                {
                    Debug.LogWarning("Can not be null");
                }
                else
                {
                    Debug.Log("from hand: " + result.name);
                }
            }
            return result;
        }
    }
}