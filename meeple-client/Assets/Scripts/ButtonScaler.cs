using System;
using UnityEngine;
using UnityEngine.UI;

namespace MeepleClient
{
    [ExecuteInEditMode]
    public class ButtonScaler : MonoBehaviour
    {
        [SerializeField] private RectTransform textRect;
        [SerializeField] private ContentSizeFitter fitter;
        private void Update()
        {
            if (textRect != null)
            {
                fitter.enabled = !fitter.enabled;
                transform.localScale = Vector3.right * LayoutUtility.GetPreferredSize(textRect, 0) +
                                       Vector3.up * 0.03f + 
                                       Vector3.forward * LayoutUtility.GetPreferredSize(textRect, 1);
                fitter.enabled = !fitter.enabled;
            }
        }
    }
}