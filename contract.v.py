# Ethereum flashcards smart-contract v1.0
#
# This contract is used to place/execute sell and buy
# orders for flashcard categories in codingchili/flashcards-webapp.git

# maps category owners to a category and its cost.
categories: {
    category: bytes32,
    owner: address,
    cost: wei_value,
}[bytes32]

# maps buyers to categories for successful purchases.
orders: {
    buyer: address,
    category: bytes32,
}[address]

# a fee in percentage 0.0-1.0 to deduct from purchases
fee: public(decimal)
# the address of the broker, for arbitrage.
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
    self.categories[categoryId].cost = cost;
    self.categories[categoryId].owner = msg.sender;
    self.categories[categoryId].category = categoryId;
    pass

# changes the cost of a category, cannot change the address!
@public
def update(categoryId: bytes32, cost: wei_value):
    assert self.categories[categoryId]
    assert self.categories[categoryId].owner == msg.sender
    self.categories[categoryId].cost = cost

# attempts to purchase the specified category.
@public
@payable
def purchase(categoryId: bytes32):
    # ensure the payment is appropriate.
    assert msg.value == self.categories[categoryId].cost;

    # ensure the sender does not already own the category.
    assert not (self.orders[msg.sender])

    # transfer funds to owner and the broker.
    send(self.broker, as_wei_value(msg.value * self.fee, wei))
    send(self.categories[categoryId].owner, as_wei_value(msg.value * (1 - self.fee), wei))

    # add the order: todo: enable one-to-many mappings.
    self.orders[msg.sender].category = categoryId

# returns the cost of the given category id.
@public
@constant
def cost(categoryId: bytes32) -> wei_value:
    return self.categories[categoryId].cost;

# returns true if the given address has bought
@public
@constant
def allowed(buyer: bytes32) -> bool:
    return self.orders[buyer];