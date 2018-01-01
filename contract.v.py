# Ethereum flashcards smart-contract v1.0
#
# This contract is used to place/execute sell and buy
# orders for flashcard categories in codingchili/flashcards-webapp.git

# maps category id to authors address and current cost.
categories: {
    category: bytes32,
    author: address,
    cost: wei_value,
}[bytes32]

# maps categories and users to owned, owned = orders[categoryId][userId]
orders: public(bool[bytes32][bytes32])

# bind users to addresses to prevent a category from being registered by a non-owner.
users: public(bytes32[address])
addresses: public(address[bytes32])

# a fee in percentage 0.0-1.0 to deduct from purchases
fee: public(decimal)
# the address of the broker, fees are sent to this address.
broker: public(address)

@public
def __init__():
    self.fee = 0.05
    self.broker = msg.sender

# adds a new category and assigns it to the sending address.
@public
def submit(categoryId: bytes32, cost: wei_value):
    # assert category does not exist - to prevent overwriting.
    assert not (self.categories[categoryId])
    assert self.users[msg.sender]
    self.categories[categoryId] = {cost: cost, author: msg.sender, category: categoryId}

# assigns an ethereum address with an user id if not already exists. by using this
# method the trading is opt-in and users themselves pay gas to register in the contract.
# without registration it would be possible to bind an users address/id to an arbitrary
#category id, ie. any user could set up someone elses category for trading.
@public
def join(userId: bytes32):
    assert not self.users[msg.sender]
    self.users[msg.sender] = userId
    self.addresses[userId] = msg.sender

# changes the cost of a category, cannot change the address!
@public
def update(categoryId: bytes32, cost: wei_value):
    # only the author may update the category.
    assert self.categories[categoryId].author == msg.sender
    self.categories[categoryId].cost = cost

# attempts to purchase the specified category and collects fees.
@public
@payable
def purchase(categoryId: bytes32):
    # ensure the category and user is listed
    assert self.categories[categoryId]
    # ensure the payment is appropriate.
    assert msg.value == self.categories[categoryId].cost;

    userId = self.users[msg.sender]

    # ensure the sender does not already own the category.
    assert not (self.orders[categoryId][userId])

    # transfer funds to owner and the broker.
    send(self.broker, as_wei_value(msg.value * self.fee, wei))
    send(self.categories[categoryId].author, as_wei_value(msg.value * (1 - self.fee), wei))

    # record the completed order.
    self.orders[categoryId][userId] = True

# returns the cost of the given category by its id.
@public
@constant
def cost(categoryId: bytes32) -> wei_value:
    return self.categories[categoryId].cost;

# returns true if the given user id has paid for the given category or is the author.
@public
@constant
def owned(categoryId: bytes32, userId: bytes32) -> bool:
    member = self.orders[categoryId][userId]
    author = self.users[self.categories[categoryId].author] == userId
    return member or author

# returns the address of a registered user. cannot use the address of the caller
# to determine identity, as this method should be callable both from the application
# and directly by users.
@public
@constant
def address(userId: bytes32) -> address:
    assert self.addresses[userId]
    return self.addresses[userId]
