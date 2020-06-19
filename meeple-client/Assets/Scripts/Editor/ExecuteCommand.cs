using UnityEditor;
using UnityEngine;

namespace MeepleClient.Editor
{
    [CustomEditor(typeof(MeepleWebSocket))]
    public class ExecuteCommand : UnityEditor.Editor
    {
        public override void OnInspectorGUI()
        {
            DrawDefaultInspector();
            var command = (MeepleWebSocket) target;
            if (GUILayout.Button("SendMove"))
            {
                command.SendMove();
            }

            if (GUILayout.Button("SendFlip"))
            {
                command.SendFlip();
            }

            if (GUILayout.Button("SendReady"))
            {
                command.SendReady();
            }
        }
    }
}