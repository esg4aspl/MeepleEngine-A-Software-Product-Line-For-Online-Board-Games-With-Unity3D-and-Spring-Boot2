using MeepleClient.Commands;

namespace MeepleClient.Network
{
    public interface ICommandConvertible
    {
        IInvocable ToCommand();
    }
}