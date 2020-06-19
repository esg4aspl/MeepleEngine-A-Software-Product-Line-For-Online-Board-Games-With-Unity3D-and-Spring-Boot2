using UnityEditor;
using UnityEngine;

namespace MeepleClient.Editor
{
    [CustomEditor(typeof(GameExporter))]
    public class ExportToJson : UnityEditor.Editor
    {
        public override void OnInspectorGUI()
        {
            DrawDefaultInspector();
            var exporter = (GameExporter) target;
            if (GUILayout.Button("Update"))
            {
                exporter.UpdateSerializables();
            }
        }
    }
}