using UnityEngine;

namespace AsImpL
{
    namespace Examples
    {
        public class EditorLikeCameraController : MonoBehaviour
        {
#if (UNITY_ANDROID || UNITY_IPHONE)
        void Awake()
        {
            Debug.LogWarning(GetType().Name + " cannot be used for mobile devices.");
        }
#endif
#if ((!UNITY_ANDROID && !UNITY_IPHONE) || UNITY_EDITOR)
            void Update()
            {
                float x = Input.GetAxis("Horizontal") * 0.3f;
                float y = 0.0f;
                float z = Input.GetAxis("Vertical") * 0.3f;

                if (Input.GetMouseButton(1))
                {
                    float rx = Input.GetAxis("Mouse Y") * 3.0f;
                    float ry = Input.GetAxis("Mouse X") * 3.0f;

                    Vector3 rot = transform.eulerAngles;
                    rot.x -= rx;
                    rot.y += ry;
                    transform.eulerAngles = rot;
                }
                if (Input.GetMouseButton(2))
                {
                    x -= Input.GetAxis("Mouse X") * 0.3f;
                    y -= Input.GetAxis("Mouse Y") * 0.3f;
                }
                z += Input.GetAxis("Mouse ScrollWheel") * 10;

                transform.Translate(x, y, z);
            }
#endif
        }
    }
}

