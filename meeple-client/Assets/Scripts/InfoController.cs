using System;
using System.Collections;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

namespace MeepleClient
{
    public class InfoController : MonoBehaviour
    {
        [SerializeField] private GameObject textMeshPro;
        [SerializeField] private TextMeshProUGUI text;
        private Vector3 _originalPos;

        private void Awake()
        {
            _originalPos = textMeshPro.transform.position;
        }
        
        public void GiveInfo(string message)
        {
            Debug.Log(message);
            textMeshPro.transform.position = _originalPos;
            text.text = message;
            text.enabled = true;
            text.alpha = 1;
            StartCoroutine(InfoAnimation());
        }

        private IEnumerator InfoAnimation()
        {
            yield return new WaitForSeconds(1f);
            var seq = LeanTween.sequence();
            LeanTween.moveY(textMeshPro, textMeshPro.transform.position.y + 200, 0.5f).setEaseLinear();
            seq.append(LeanTween.value(1, 0f, 0.5f).setOnUpdate((f => text.alpha = f)));
            seq.append((() => text.enabled = false));
        }
    }
}