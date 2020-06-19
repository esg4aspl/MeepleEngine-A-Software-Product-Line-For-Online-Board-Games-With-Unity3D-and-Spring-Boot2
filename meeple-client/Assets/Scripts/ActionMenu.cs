using UnityEngine;

namespace MeepleClient
{
    public class ActionMenu : MonoBehaviour
    {
        public bool show = false;
        private Camera _camera;
        public Hand Hand;
        private Vector2 mousePosition;
        private Card _card;

        private void Start()
        {
            _camera = Camera.main;
        }

        private void Update()
        {
            if (Input.GetMouseButtonDown(1))
            {
                _card = GetNearestObject()?.GetComponent<Card>();
                show = _card != null;
                mousePosition = Input.mousePosition;
            }
            else if (Input.GetMouseButtonDown(0))
            {
                // Debug.Log("bom");
                show = false;
            }
        }

        private void OnGUI()
        {
            if (show && GUI.Button(new Rect(mousePosition.x, Screen.height - mousePosition.y, 50, 50), "Draw"))
            {
                Debug.Log("bom2");

                Hand.AddCard(_card);
                show = false;
            }
        }

        private GameObject GetNearestObject()
        {
            // TODO change camera to local player camera
            GameObject result = null;
            var ray = _camera.ScreenPointToRay(mousePosition);
            Debug.DrawRay(ray.origin, ray.direction, Color.cyan);
            if (Physics.Raycast(ray, out var hit, 100, LayerMask.GetMask("MeepleObject")))
            {
                result = hit.transform.gameObject;
                Debug.Log(result.name);
            }

            return result;
        }
    }
}