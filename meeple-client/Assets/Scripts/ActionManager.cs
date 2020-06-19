using MeepleClient.Commands;
using MeepleClient.Core;
using MeepleClient.Network;
using UnityEngine;

namespace MeepleClient
{
    
    
    public class ActionManager : MonoBehaviour
    {
        // Start is called before the first frame update
        [SerializeField] private MeepleWebSocket webSocket;
        [SerializeField] private HandController handController;
        private Camera _camera;

        [SerializeField, ReadOnly] private Item _selectedObject;
        [SerializeField, ReadOnly] private Grid _destinationObject;
        [SerializeField, ReadOnly] private LayerMask gridMask;
        [SerializeField, ReadOnly] private LayerMask meepleObjectMask;
        
        [SerializeField, ReadOnly] private bool isSelected;

        public HandController HandController
        {
            get => handController;
            set => handController = value;
        }


        void Start()
        {
            _camera = Camera.main;
            gridMask = LayerMask.GetMask("Grid");
            meepleObjectMask = LayerMask.GetMask("MeepleObject");
        }

        // Update is called once per frame
        void Update()
        {
            if (Input.GetMouseButtonDown(0))
            {
                OnLeftClick();
                // GameObject selectable = null;
                // if (handController != null && handController.IsOnHand())
                // {
                //     selectable = handController.GetNearestObjectFromHand();
                // }
                // else
                // {
                //     selectable = GetNearestObject(meepleObjectMask);
                // }

                // _selectedObject = selectable?.GetComponent<IInteractable>();
            }

            else if (Input.GetMouseButtonDown(1))
            {
                OnRightClick();
            }

            else if (Input.GetKeyDown(KeyCode.F))
            {
                OnFPressed();
            }
        }

        private void OnFPressed()
        {
            Debug.Log("F pressed");
            if (isSelected)
            {
                webSocket.SendAction(new FlipCommand(_selectedObject));
            }
        }

        private void OnRightClick()
        {
            if (isSelected)
            {
                var grid = Click<Grid>(gridMask);
                if (grid != null)
                {
                    GoToDestination(grid);
                }
            }
            else
            {
                var item = Click<Item>(meepleObjectMask);
                if (item != null)
                {
                    Show(item);
                }
            }
        }

        private void OnLeftClick()
        {
            Deselect();
            Item item;
            if (HandController != null && HandController.IsOnHand())
            {
                item = HandController.Click<Item>(meepleObjectMask);
            }
            else
            {
                item = Click<Item>(meepleObjectMask);
            }
            if (item != null)
            {
                Select(item);
            }
        }

        private void Select(Item item)
        {
            isSelected = true;
            _selectedObject = item;
            Debug.Log($"Selected {_selectedObject.name}, {_selectedObject.Guid}");
            var button = item as MeepleButton;
            if (button != null)
            {
                webSocket.SendMessage(new ClickMessage(button.name));
            }
            else
            {
                webSocket.SendMessage(new SelectMessage(_selectedObject.Guid.ToString()));
            }
            // TODO highlight selected object
        }

        private void GoToDestination(Grid grid)
        {
            _destinationObject = grid;
            Debug.Log($"Moving {_selectedObject.name}, {_selectedObject.Guid} TO {_destinationObject.name}, {_destinationObject.Guid}");
            webSocket.SendAction(new MoveCommand(_selectedObject, _destinationObject));
        }
        

        private void Show(Item meepleObject)
        {
            switch (meepleObject)
            {
                case Deck deck:
                    Debug.Log("Open deck options menu");
                    break;
                
                case Card card:
                    Debug.Log("Show card options");
                    break;
            }            
        }

        private T Click<T>(LayerMask layerMask) where T: MeepleObject
        {
            T result = null;
            var ray = _camera.ScreenPointToRay(Input.mousePosition);
            if (Physics.Raycast(ray, out var hit, 100, layerMask))
            {
                result = hit.transform.gameObject.GetComponent<T>();
                if (result == null)
                {
                    Debug.LogWarning("Can not be null");
                    Deselect();
                }
                Debug.Log(result.name);
            }
            else
            {
                Deselect();
            }
            return result;
        }

        private void Deselect()
        {
            if (isSelected)
            {
                Debug.Log($"Deselected {_selectedObject.name}, {_selectedObject.Guid}");
                _selectedObject = null;
                isSelected = false;
            }
        }
    }
}