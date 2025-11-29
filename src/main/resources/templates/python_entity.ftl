from sqlalchemy import Column, Integer, String, DateTime, Enum
from sqlalchemy.ext.declarative import declarative_base
from datetime import datetime
<#if hasStateEnum>
from enum import Enum as PyEnum
</#if>

Base = declarative_base()

<#if hasStateEnum>
class ${stateEnum.name}(PyEnum):
<#list stateEnum.values as value>
    ${value.name} = "${value.description}"
</#list>

</#if>
class ${className}(Base):
    __tablename__ = '${tableName}'
    
<#list attributes as attr>
    ${attr.name} = Column(${attr.sqlType}<#if attr.isPrimary>, primary_key=True</#if>)
</#list>
<#if hasStateEnum>
    status = Column(Enum(${stateEnum.name}))
</#if>
    created_at = Column(DateTime, default=datetime.utcnow)
    updated_at = Column(DateTime, default=datetime.utcnow, onupdate=datetime.utcnow)

<#if hasTransitionMethods>
<#list transitionMethods as method>
    def ${method.name}(self):
        """${method.trigger}"""
        # State transition logic
        pass
        
</#list>
</#if>