using MeepleClient.Commands;

namespace MeepleClient.Network
{
    public class ClickMessage: Message
    {
        public override string Channel { get; } = "Click";
        public ClickData Data { get; set; }

        public ClickMessage(string buttonName)
        {
            Data = new ClickData()
            {
                ButtonName = buttonName
            };
        }
    }

    public class ClickData
    {
        public string ButtonName { get; set; }
    }
}