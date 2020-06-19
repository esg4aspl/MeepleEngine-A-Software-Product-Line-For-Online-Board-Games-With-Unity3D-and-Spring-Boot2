using UnityEditor;
using UnityEngine;

namespace MeepleClient.Editor
{
    [CustomEditor(typeof(Card)), CanEditMultipleObjects]
    public class UpdateCard : UnityEditor.Editor
    {
        public override void OnInspectorGUI()
        {
            DrawDefaultInspector();
            var card = (Card) target;
            if (GUILayout.Button("Update"))
            {
                card.UpdateTexture();
            }
        }
    }
}