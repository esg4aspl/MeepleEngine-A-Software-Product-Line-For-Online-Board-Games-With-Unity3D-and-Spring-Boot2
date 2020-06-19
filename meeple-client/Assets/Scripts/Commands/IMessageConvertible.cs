using MeepleClient.Network;

namespace MeepleClient.Commands
{
    public interface IMessageConvertible
    {
        Message ToMessage();
    }
}