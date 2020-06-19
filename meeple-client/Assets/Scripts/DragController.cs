using UnityEngine;

namespace MeepleClient
{
    public class DragController : MonoBehaviour

    {
        public float liftAmount = 1f;
        private Vector3 _offset;
        private float _zCoordinate;
        private float _yCoordinate;
        private Camera _camera;
        private MeepleObject _ınterectable;

        private void Awake()
        {
            _ınterectable = GetComponent<MeepleObject>();
            // _camera = Camera.main;
            if (_camera == null)
            {
                _camera = FindObjectOfType<Camera>();
            }
        }

        private void Update()
        {
        }

        private void OnMouseUp()
        {
            // _ınterectable.IsHolding = false;
            GetComponent<Rigidbody>().useGravity = true;
        }

        void OnMouseDown()

        {
            // _ınterectable.IsHolding = true;
            GetComponent<Rigidbody>().useGravity = false;
            // LeanTween.moveLocalY(gameObject, transform.position.y + liftAmount, 0.5f).setEaseOutQuad();
            transform.rotation = Quaternion.Euler(new Vector3(0, 0, transform.rotation.z));
            _zCoordinate = _camera.WorldToScreenPoint(
                transform.position).z;
            _yCoordinate = transform.position.y + liftAmount;

            // Store offset = gameobject world pos - mouse world pos

            _offset = transform.position - GetMouseAsWorldPoint();
        }


        private Vector3 GetMouseAsWorldPoint()

        {
            // Pixel coordinates of mouse (x,y)

            Vector3 mousePoint = Input.mousePosition;


            // z coordinate of game object on screen

            mousePoint.z = _zCoordinate;


            // Convert it to world points

            return _camera.ScreenToWorldPoint(mousePoint);
        }


        void OnMouseDrag()

        {
            var mousePoint = GetMouseAsWorldPoint();
            mousePoint.y = _yCoordinate;
            transform.position = mousePoint + _offset;
            CalculateHeight();
        }

        private void CalculateHeight()
        {
            var ray = new Ray(transform.position + 5 * Vector3.down, Vector3.down);
            if (Physics.Raycast(ray, out var hit))
            {
                if (hit.transform.gameObject == this.gameObject) return;

                // Debug.DrawLine();
                Debug.DrawLine(transform.position, transform.position + Vector3.down * hit.distance);
                var correction = liftAmount - 5 - hit.distance;
                Debug.Log(correction);
                if (correction > 1)
                    _yCoordinate += correction;
            }
        }
    }
}